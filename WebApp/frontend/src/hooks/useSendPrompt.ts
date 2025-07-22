import type { ChatMessage } from "../types/chatMessage";
import sendPrompt from "../utils/sendPrompt";
import handleStartConnection from "./useSseResponse";

export default async function handleSubmit(
  prompt: string,
  setPrompt: React.Dispatch<React.SetStateAction<string>>,
  setMessages: React.Dispatch<React.SetStateAction<ChatMessage[]>>,
  sendingRef: React.RefObject<boolean>
) {
  if (prompt.trim() === "") {
    console.error("Can't send an empty prompt.");
    return;
  }

  console.log("Sending:", prompt);

  const result = await sendPrompt("default", prompt);

  if (!result) {
    console.error("An error occurred during response from the server.");
    return;
  }

  setMessages((prev) => [...prev, { type: "prompt", content: prompt }]);
  setPrompt("");
  handleStartConnection(setMessages, sendingRef);
}
