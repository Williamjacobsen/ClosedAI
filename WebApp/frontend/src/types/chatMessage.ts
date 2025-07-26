export type ChatMessage = {
  type: "prompt" | "response";
  message: string;
  chatSessionName?: string;
};
