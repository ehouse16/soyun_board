package board.soyun_board.mapper;

import board.soyun_board.dto.post.PostCreateDto;
import board.soyun_board.dto.post.PostResponseDto;
import board.soyun_board.entity.post.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T16:21:09+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post toPostFromPostCreateDto(PostCreateDto postCreateDto) {
        if ( postCreateDto == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        post.title( postCreateDto.getTitle() );
        post.content( postCreateDto.getContent() );
        post.author( postCreateDto.getAuthor() );

        return post.build();
    }

    @Override
    public PostResponseDto toPostResponseDtofromPost(Post post) {
        if ( post == null ) {
            return null;
        }

        PostResponseDto.PostResponseDtoBuilder postResponseDto = PostResponseDto.builder();

        postResponseDto.id( post.getId() );
        postResponseDto.title( post.getTitle() );
        postResponseDto.content( post.getContent() );
        postResponseDto.author( post.getAuthor() );

        return postResponseDto.build();
    }
}
