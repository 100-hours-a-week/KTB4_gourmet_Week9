package KTB4_gourmet_Week9.Assignment.repository;

import KTB4_gourmet_Week9.Assignment.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);

    void deleteByPost_Id(Long postId);

    void deleteByUser_Id(Long userId);
}