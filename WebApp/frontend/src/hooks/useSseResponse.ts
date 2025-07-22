import { createSSEConnection } from "../utils/sse";
import type { ChatMessage } from "../types/chatMessage";

export default function handleStartConnection(
  setMessages: React.Dispatch<React.SetStateAction<ChatMessage[]>>
) {
  createSSEConnection({
    url: "http://localhost:8080/get-response",
    withCredentials: true,

    onMessage: (event) => {
      if (event.data === "Connection Established...") {
        return;
      }

      if (typeof event.data !== "string") {
        console.error("Expected a string from SSE, got:", typeof event.data);
        return;
      }

      setMessages((prev) => [
        ...prev,
        { type: "response", content: event.data },
      ]);

      console.log(`Received response: ${event.data}`);
    },

    eventName: "response",

    onOpen: () => console.log("Connection established"),
    onError: (error) => console.error("onerror", error),
    onClose: () => console.log("Connection closed by server."),
  });
}
