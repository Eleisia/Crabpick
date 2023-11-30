import React, { useState } from "react";
import styled from "styled-components";

const CommentBox = styled.textarea`
  width: 100%;
  /* width: fit-content; */
  resize: none;
  height: 100px;
  font-size: 16px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const CommentButton = styled.input`
  background-color: #4caf50;
  color: white;
  padding: 10px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

const Comment: React.FC = () => {
  const [comment, setComment] = useState("");

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(comment);
    // Add comment submission logic here
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <CommentBox
          value={comment}
          onChange={(event: any) => setComment(event.target.value)}
        />
        <div style={{ display: "flex", justifyContent: "flex-end" }}>
          <CommentButton type="submit" value="등록" />
        </div>
      </form>
    </div>
  );
};

export default Comment;
