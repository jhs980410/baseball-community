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
        private Boolean isHidden;

        public static AdminPostDto fromEntity(AdminPost entity) {
            return AdminPostDto.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .userId(entity.getId())
                    .teamId(entity.getTeamId())
                    .likeCount(entity.getStatus().getLikeCount())
                    .build();
        }
        public AdminPostDto(
                Long id,
                String title,
                Long userId,
                Long teamId,
                Long commentCount,
                Long likeCount,
                Long viewCount,
                Boolean isHidden
        ) {
            this.id = id;
            this.title = title;
            this.userId = userId;
            this.teamId = teamId;
            this.likeCount = likeCount;
            this.isHidden = isHidden;
            // commentCount, viewCount는 DTO에 현재 필드 없으면 그냥 무시되어도 OK
        }

        public AdminPostDto(Long id,
                            String title,
                            Long userId,
                            Long teamId,
                            Integer commentCount,
                            Integer likeCount,
                            Integer viewCount,
                            Boolean isHidden) {
            this.id = id;
            this.title = title;
            this.userId = userId;
            this.teamId = teamId;
            this.likeCount = likeCount != null ? likeCount.longValue() : 0L; // 필요 시 변환
            this.isHidden = isHidden;
        }


    }
