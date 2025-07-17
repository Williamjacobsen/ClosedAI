import axios from "axios";

export default async function sendPrompt(
  chatSessionName: string,
  prompt: string
) {
  return axios
    .post(
      "http://localhost:8080/prompt/send",
      {
        chatSessionName: chatSessionName,
        prompt: prompt,
      },
      {
        withCredentials: true,
      }
    )
    .then((response) => {
      return response;
    })
    .catch((error) => {
      console.error(error);
      return;
    });
}
