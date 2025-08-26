// useOutsideClick.ts
import { useEffect } from "react";

export default function useOutsideClick(
  ref: React.RefObject<HTMLElement | null>, // <- null 허용
  callback: () => void
) {
  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        callback();
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [ref, callback]);
}
