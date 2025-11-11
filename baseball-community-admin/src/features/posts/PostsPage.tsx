import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message, Modal, Descriptions } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import type { Post } from "../../types/post";

const PostsPage: React.FC = () => {
  const [data, setData] = useState<Post[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedPost, setSelectedPost] = useState<Post | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  /** 게시글 목록 불러오기 */
  const fetchPosts = async () => {
    setLoading(true);
    try {
      const response = await axios.get("/api/admin/posts", { withCredentials: true });
      setData(response.data.content);
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
      const res = await axios.get(`/api/admin/posts/${id}`, { withCredentials: true });
      setSelectedPost(res.data);
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
      await axios.patch(`/api/admin/posts/${id}/restore`, {}, { withCredentials: true });
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
  const columns: ColumnsType<Post> = [
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
      render: (flagged: boolean) =>
        flagged ? <Tag color="volcano">플래그됨</Tag> : <Tag color="blue">정상</Tag>,
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
      <Table<Post>
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
            <Descriptions.Item label="작성자 ID">{selectedPost.userId}</Descriptions.Item>
            <Descriptions.Item label="팀">{selectedPost.teamId}</Descriptions.Item>
            <Descriptions.Item label="제목">{selectedPost.title}</Descriptions.Item>
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
