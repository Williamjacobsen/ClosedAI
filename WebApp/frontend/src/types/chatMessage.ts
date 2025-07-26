export type ChatMessage = {
  type: "PROMPT" | "RESPONSE";
  message: string;
  chatSessionName?: string;
};
