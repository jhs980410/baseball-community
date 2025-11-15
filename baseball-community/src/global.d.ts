// CSS 파일 타입 선언
declare module "*.css";
declare module "react-quill/dist/quill.snow.css";

// 🔥 axios 타입 확장 (빌드 오류 해결 핵심!)
import "axios";

declare module "axios" {
  export interface AxiosResponse<T = any> {
    data: T;
  }
}
