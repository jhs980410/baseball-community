package com.baseball.baseballcommunitybe.admin.dto.post;



import com.baseball.baseballcommunitybe.admin.entity.AdminPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminPostDto {
    private Long id;
    private String title;
    private Long userId;
    private Long teamId;
    private Long likeCount;

    public static AdminPostDto fromEntity(AdminPost entity) {
        return AdminPostDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .userId(entity.getId())
                .teamId(entity.getTeamId())
                .likeCount(entity.getStatus().getLikeCount())
                .build();
    }
}
