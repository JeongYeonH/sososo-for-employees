import React from 'react';

interface TextWithEllipsisProps {
  text: string | null | undefined;
  maxLength?: number;
}

function TextWithEllipsis({ text, maxLength = 20 }: TextWithEllipsisProps) {
  if (!text) {
    return <span>아직 대회가 없습니다.</span>;
  }
  
  
  return (
    <span>
      {text.length > maxLength ? text.substring(0, maxLength) + "..." : text}
    </span>
  );
}

export default TextWithEllipsis;