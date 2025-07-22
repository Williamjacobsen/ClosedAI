export type ChatMessage = {
  type: "prompt" | "response";
  content: string;
};
