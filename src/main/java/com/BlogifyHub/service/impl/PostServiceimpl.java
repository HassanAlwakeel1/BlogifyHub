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
import com.BlogifyHub.service.PostService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceimpl implements PostService {

    private PostRepository postRepository;

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    private PostMapper postMapper;

    private SubscriptionRepository subscriptionRepository;
    private  JavaMailSender mailSender;


    public PostServiceimpl(PostRepository postRepository,
                           CategoryRepository categoryRepository,
                           UserRepository userRepository,
                           PostMapper postMapper,
                           SubscriptionRepository subscriptionRepository,
                           JavaMailSender mailSender){
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.subscriptionRepository = subscriptionRepository;
        this.mailSender = mailSender;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO, Long userId) {
        Post post = postMapper.mapToEntity(postDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        post.setUser(user);
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        Post newPost = postRepository.save(post);
        PostDTO postRespose = postMapper.mapToDTO(newPost);
        postRespose.setProfileResponseDTO(profileResponseDTO);

        // Notify subscribers
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

        return postRespose;
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
