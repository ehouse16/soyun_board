package board.soyun_board.mapper;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostResponseDto;
import board.soyun_board.entity.Post;
import ch.qos.logback.core.model.ComponentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    //from PostCreateDto to Post
    Post toPostFromPostCreateDto(PostCreateDto postCreateDto);

    //from Post ro PostResponseDto
    PostResponseDto toPostResponseDtofromPost(Post post);
}
