import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message, Modal, Descriptions } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";

// 🔹 이 파일에서만 사용할 Admin 전용 타입 (백엔드 JSON 형태에 맞게 카멜케이스)
type AdminPostStatus = {
  flagged: boolean;
  lastFlagReason?: string | null;
};

type AdminPost = {
  id: number;
  userId: number;   // 작성자 ID
  teamId: number;   // 팀 ID
  title: string;
  content: string;
  isHidden: boolean;           // 숨김 여부
  status?: AdminPostStatus;    // 관리자용 상태 정보 (플래그 등)
};

const PostsPage: React.FC = () => {
  const [data, setData] = useState<AdminPost[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedPost, setSelectedPost] = useState<AdminPost | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  /** 게시글 목록 불러오기 */
  const fetchPosts = async () => {
    setLoading(true);
    try {
      // 🔥 응답 타입을 any로 강제 → res.data.content 타입 에러 방지
      const response: any = await axios.get("/api/admin/posts", {
        withCredentials: true,
      });

      // 서버에서 content 배열이 케이스 섞여서 올 수도 있으니 안전하게 매핑
      const list: AdminPost[] = (response.data.content || []).map((p: any) => ({
        id: p.id,
        userId: p.userId ?? p.user_id,
        teamId: p.teamId ?? p.team_id,
        title: p.title,
        content: p.content,
        isHidden: p.isHidden ?? p.is_hidden ?? false,
        status: p.status
          ? {
              flagged: p.status.flagged ?? false,
              lastFlagReason: p.status.lastFlagReason ?? p.status.last_flag_reason ?? null,
            }
          : undefined,
      }));

      setData(list);
    } catch (err) {
      message.error("게시글을 불러오는 중 오류가 발생했습니다.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  /** 단건 조회 (보기 클릭 시) */
  const handleView = async (id: number) => {
    try {
      const res: any = await axios.get(`/api/admin/posts/${id}`, {
        withCredentials: true,
      });

      const p = res.data;
      const detail: AdminPost = {
        id: p.id,
        userId: p.userId ?? p.user_id,
        teamId: p.teamId ?? p.team_id,
        title: p.title,
        content: p.content,
        isHidden: p.isHidden ?? p.is_hidden ?? false,
        status: p.status
          ? {
              flagged: p.status.flagged ?? false,
              lastFlagReason: p.status.lastFlagReason ?? p.status.last_flag_reason ?? null,
            }
          : undefined,
      };

      setSelectedPost(detail);
      setIsModalOpen(true);
    } catch (err) {
      message.error("게시글 상세 정보를 불러오지 못했습니다.");
      console.error(err);
    }
  };

  /** 숨김 처리 (DELETE) */
  const handleHide = async (id: number) => {
    try {
      await axios.delete(`/api/admin/posts/${id}`, { withCredentials: true });
      message.success("게시글이 숨김 처리되었습니다.");
      fetchPosts();
    } catch (err) {
      message.error("숨김 처리 중 오류가 발생했습니다.");
      console.error(err);
    }
  };

  /** 복구 처리 (PATCH) */
  const handleRestore = async (id: number) => {
    try {
      await axios.patch(
        `/api/admin/posts/${id}/restore`,
        {},
        { withCredentials: true }
      );
      message.success("게시글이 복구되었습니다.");
      fetchPosts();
    } catch (err) {
      message.error("복구 중 오류가 발생했습니다.");
      console.error(err);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, []);

  /** 테이블 컬럼 정의 */
  const columns: ColumnsType<AdminPost> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "작성자 ID", dataIndex: "userId", key: "userId" },
    { title: "팀", dataIndex: "teamId", key: "teamId" },
    { title: "제목", dataIndex: "title", key: "title" },
    {
      title: "상태",
      dataIndex: "isHidden",
      key: "isHidden",
      render: (isHidden: boolean) =>
        isHidden ? <Tag color="red">숨김</Tag> : <Tag color="green">노출</Tag>,
    },
    {
      title: "플래그",
      dataIndex: ["status", "flagged"],
      key: "flagged",
      render: (flagged: boolean | undefined) =>
        flagged ? (
          <Tag color="volcano">플래그됨</Tag>
        ) : (
          <Tag color="blue">정상</Tag>
        ),
    },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleView(record.id)}>
            보기
          </Button>
          {record.isHidden ? (
            <Button type="link" onClick={() => handleRestore(record.id)}>
              복구
            </Button>
          ) : (
            <Button type="link" danger onClick={() => handleHide(record.id)}>
              숨김
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h2>📝 게시글 관리</h2>
      <Table<AdminPost>
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="id"
        pagination={{ pageSize: 10 }}
      />

      {/* 게시글 상세 모달 */}
      <Modal
        title={`게시글 #${selectedPost?.id} 상세 보기`}
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        footer={[
          <Button key="close" onClick={() => setIsModalOpen(false)}>
            닫기
          </Button>,
        ]}
        width={800}
      >
        {selectedPost ? (
          <Descriptions bordered column={1} size="small">
            <Descriptions.Item label="작성자 ID">
              {selectedPost.userId}
            </Descriptions.Item>
            <Descriptions.Item label="팀">
              {selectedPost.teamId}
            </Descriptions.Item>
            <Descriptions.Item label="제목">
              {selectedPost.title}
            </Descriptions.Item>
            <Descriptions.Item label="내용">
              <div
                dangerouslySetInnerHTML={{ __html: selectedPost.content }}
                style={{ whiteSpace: "pre-wrap" }}
              />
            </Descriptions.Item>
            <Descriptions.Item label="상태">
              {selectedPost.isHidden ? (
                <Tag color="red">숨김</Tag>
              ) : (
                <Tag color="green">노출</Tag>
              )}
            </Descriptions.Item>
            {selectedPost.status && (
              <>
                <Descriptions.Item label="플래그 여부">
                  {selectedPost.status.flagged ? "O" : "X"}
                </Descriptions.Item>
                <Descriptions.Item label="최근 플래그 사유">
                  {selectedPost.status.lastFlagReason || "-"}
                </Descriptions.Item>
              </>
            )}
          </Descriptions>
        ) : (
          <p>데이터를 불러오는 중...</p>
        )}
      </Modal>
    </div>
  );
};

export default PostsPage;
