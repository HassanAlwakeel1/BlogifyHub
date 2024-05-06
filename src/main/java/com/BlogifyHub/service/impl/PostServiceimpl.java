package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.PostDTO;
import com.BlogifyHub.model.DTO.PostResponseDTO;
import com.BlogifyHub.model.entity.Category;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.repository.CategoryRepository;
import com.BlogifyHub.repository.PostRepository;
import com.BlogifyHub.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceimpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;

    private CategoryRepository categoryRepository;

    public PostServiceimpl(PostRepository postRepository,
                           ModelMapper modelMapper,
                           CategoryRepository categoryRepository){
        this.postRepository = postRepository;
        this.mapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = mapToEntity(postDTO);
        Post newPost = postRepository.save(post);
        PostDTO postRespose = mapToDTO(newPost);
        return postRespose;
    }

    @Override
    public PostResponseDTO getAllPosts(int pageNumber,int pageSize,String sortBy, String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> posts =postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDTO> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

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

        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map((post -> mapToDTO(post)))
                .collect(Collectors.toList());
    }

    private PostDTO mapToDTO(Post post){
        PostDTO postDto = mapper.map(post,PostDTO.class);
        return postDto;
    }

    private Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO,Post.class);
        return post;
    }
}
