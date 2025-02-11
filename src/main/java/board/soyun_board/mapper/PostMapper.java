package board.soyun_board.mapper;

import board.soyun_board.dto.post.PostCreateDto;
import board.soyun_board.dto.post.PostResponseDto;
import board.soyun_board.entity.post.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    //from PostCreateDto to Post
    Post toPostFromPostCreateDto(PostCreateDto postCreateDto);

    //from Post ro PostResponseDto
    PostResponseDto toPostResponseDtofromPost(Post post);
}
