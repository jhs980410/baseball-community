import React from "react";
import "../styles/Button.css";

type ButtonProps = {
  children: React.ReactNode;
  onClick?: () => void;
  variant?: "primary" | "secondary";
  size?: "small" | "medium" | "large";
};

export default function Button({
  children,
  onClick,
  variant = "primary",
  size = "medium",
}: ButtonProps) {
  return (
    <button
      className={`btn ${variant} ${size}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
}
