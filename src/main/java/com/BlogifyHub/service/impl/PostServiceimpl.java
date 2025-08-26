package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.BlogAPIException;
import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostResponseDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Category;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.Subscription;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.mapper.PostMapper;
import com.BlogifyHub.repository.CategoryRepository;
import com.BlogifyHub.repository.PostRepository;
import com.BlogifyHub.repository.SubscriptionRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.security.RequiresActiveAccount;
import com.BlogifyHub.service.CloudinaryImageService;
import com.BlogifyHub.service.PostService;
import com.BlogifyHub.utility.FileMultipartFile;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;


@Service
public class PostServiceimpl implements PostService {

    private PostRepository postRepository;

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    private PostMapper postMapper;

    private SubscriptionRepository subscriptionRepository;
    private  JavaMailSender mailSender;

    private CloudinaryImageService cloudinaryImageService;


    public PostServiceimpl(PostRepository postRepository,
                           CategoryRepository categoryRepository,
                           UserRepository userRepository,
                           PostMapper postMapper,
                           SubscriptionRepository subscriptionRepository,
                           JavaMailSender mailSender,
                           CloudinaryImageService cloudinaryImageService){
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.subscriptionRepository = subscriptionRepository;
        this.mailSender = mailSender;
        this.cloudinaryImageService = cloudinaryImageService;
    }

    @Override
    @RequiresActiveAccount
    public PostDTO createPost(PostDTO postDTO, Long userId) {
        Post post = postMapper.mapToEntity(postDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        post.setUser(user);

        String processedContent = processContentImages(post.getContent());
        post.setContent(processedContent);

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        Post newPost = postRepository.save(post);
        PostDTO postResponse = postMapper.mapToDTO(newPost);
        postResponse.setProfileResponseDTO(profileResponseDTO);

        notifySubscribers(user, post);

        return postResponse;
    }

    private String processContentImages(String content) {
        Pattern pattern = Pattern.compile("<img src='(.*?)'");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            try {
                File imageFile = downloadImageToFile(imageUrl);
                MultipartFile multipartFile = convertFileToMultipartFile(imageFile);
                Map<String, Object> uploadResult = cloudinaryImageService.upload(multipartFile);
                String cloudinaryUrl = uploadResult.get("url").toString();
                content = content.replace(imageUrl, cloudinaryUrl);

                // Optionally delete the temporary file after upload
                imageFile.delete();
            } catch (Exception e) {
                throw new RuntimeException("Image processing failed for URL: " + imageUrl, e);
            }
        }
        return content;
    }


    private File downloadImageToFile(String imageUrl) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            File tempFile = File.createTempFile("downloaded-image", ".jpg");
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                in.transferTo(out);
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to download image", e);
        }
    }

    private MultipartFile convertFileToMultipartFile(File file) {
        return new FileMultipartFile(file, "image/jpeg");
    }

    private void notifySubscribers(User user, Post post) {
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);
        for (Subscription subscription : subscriptions) {
            User subscriber = subscription.getSubscriber();
            String subject = "New Post from " + user.getFirstName() + " " + user.getLastName();
            String body = "Dear " + subscriber.getFirstName() + ",<br><br>" +
                    user.getFirstName() + " " + user.getLastName() + " has posted a new article titled: " +
                    post.getTitle() + ".<br><br>" +
                    "Description: " + post.getDescription() + "<br><br>" +
                    "Read more on BlogifyHub.<br><br>" +
                    "Best regards,<br>BlogifyHub Team";

            sendEmail(subscriber.getEmail(), subject, body);
        }
    }
    private void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public PostResponseDTO getAllPosts(int pageNumber,
                                       int pageSize,
                                       String sortBy,
                                       String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> posts =postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDTO> content = listOfPosts.stream()
                .map(post -> {
                    PostDTO postDTO = postMapper.mapToDTO(post);
                    User user = post.getUser();
                    ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getBio(),
                            user.getProfilePictureURL(),
                            user.getFollowersNumber()
                    );
                    postDTO.setProfileResponseDTO(profileResponseDTO);
                    return postDTO;
                })
                .collect(Collectors.toList());


        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setContent(content);
        postResponseDTO.setPageNumber(posts.getNumber());
        postResponseDTO.setPageSize(posts.getSize());
        postResponseDTO.setTotalElements(posts.getTotalElements());
        postResponseDTO.setTotalPages(posts.getTotalPages());
        postResponseDTO.setLastPage(posts.isLast());

        return postResponseDTO;
    }

    @Override
    public PostDTO getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        User user = post.getUser();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        PostDTO postRespose = postMapper.mapToDTO(post);
        postRespose.setProfileResponseDTO(profileResponseDTO);
        return postRespose;
    }

    @Override
    @RequiresActiveAccount
    public PostDTO updatePost(PostDTO postDTO, long userId, long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        if (post.getUser().equals(user)){
            post.setTitle(postDTO.getTitle());
            post.setDescription(postDTO.getDescription());
            post.setContent(postDTO.getContent());

            ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBio(),
                    user.getProfilePictureURL(),
                    user.getFollowersNumber()
            );
            PostDTO postRespose = postMapper.mapToDTO(post);
            postRespose.setProfileResponseDTO(profileResponseDTO);
            postRepository.save(post);
            return postRespose;
        }
        /**
         * Throws an exception directly as a safety measure because
         * the method will not reach this point if successful, terminating
         * on the return statement prior.
         */
        throw new BlogAPIException(HttpStatus.FORBIDDEN,"You are not authorized to update this post.");
    }


    /**
     * Places the 'else' clause before throwing the exception because there is no return statement.
     * In case of success, the method will continue execution to the end, triggering the throw
     * in both success and failure scenarios. Placing it within an 'if-else' block would prevent
     * the throw from occurring in case of success.
     */
    @Override
    @RequiresActiveAccount
    public void deletePostById(long userId, long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        if (post.getUser().equals(user)){
            postRepository.delete(post);
        }else throw new BlogAPIException(HttpStatus.FORBIDDEN,
                "You are not authorized to update this post.");
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        List<PostDTO> postDTOList = posts.stream()
                .map(post -> {
                    PostDTO postDTO = postMapper.mapToDTO(post);
                    User user = post.getUser();
                    ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getBio(),
                            user.getProfilePictureURL(),
                            user.getFollowersNumber()
                    );
                    postDTO.setProfileResponseDTO(profileResponseDTO);
                    return postDTO;
                })
                .collect(Collectors.toList());
        return postDTOList;
    }
}