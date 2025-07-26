import { createSSEConnection } from "../utils/sse";
import type { ChatMessage } from "../types/chatMessage";

export default function handleStartConnection(
  setMessages: React.Dispatch<React.SetStateAction<ChatMessage[]>>,
  sendingRef: React.RefObject<boolean>
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
        { type: "RESPONSE", message: event.data },
      ]);

      console.log(`Received response: ${event.data}`);

      sendingRef.current = false;
    },

    eventName: "response",

    onOpen: () => console.log("Connection established"),
    onError: (error) => {
      console.error("onerror", error);
      sendingRef.current = false;
    },
    onClose: () => console.log("Connection closed by server."),
  });
}
