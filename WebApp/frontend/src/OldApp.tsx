/* TODO: If the backend or a microservice is down, let the user know */

import { Search } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { BallTriangle } from "react-loading-icons";

export const useSseResponses = (
  setPrompts: React.Dispatch<
    React.SetStateAction<{ id: number; value: any; type: string }[]>
  >
) => {
  const [sseDown, setSseDown] = useState(false);
  const sseRef = useRef<EventSource | null>(null);

  const connect = () => {
    if (sseRef.current) {
      sseRef.current.close();
    }

    const src = new EventSource("http://localhost:8080/get-response", {
      withCredentials: true,
    });

    sseRef.current = src;

    const onResponse = (evt: MessageEvent) => {
      try {
        console.log(`SSE Response: ${evt.data}`);

        const payload = JSON.parse(evt.data) as {
          id: number;
          value: string;
        };

        setPrompts((prev) => [
          ...prev,
          { id: payload.id, value: payload.value, type: "response" },
        ]);
      } catch (err) {
        console.error("Bad SSE payload:", evt.data);
      }
    };

    src.addEventListener("response", onResponse);

    src.onerror = () => {
      setSseDown(true);
      src.close();
    };
  };

  useEffect(() => {
    return () => {
      if (sseRef.current) {
        sseRef.current.close();
      }
    };
  }, []);

  return { sseDown, connect };
};

function App() {
  /* States */
  const textareaRef = useRef<HTMLTextAreaElement | null>(null);
  const [inputText, setInputText] = useState("");
  const [prompts, setPrompts] = useState<
    { id: number; value: any; type: string }[]
  >([]);
  const { sseDown, connect } = useSseResponses(setPrompts);

  /* Automatically scroll down */
  const divScrollDownRef = useRef<null | HTMLDivElement>(null);
  useEffect(() => {
    if (divScrollDownRef.current) {
      divScrollDownRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [prompts]);

  /** Handle prompt input scaling */
  const handleInput = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = "auto";
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  };

  /** Send input prompt & instantly show input prompt when send */
  const handleSubmit = async () => {
    try {
      /* TODO: Fix id bug */

      const lastId = Number(prompts.at(-1)?.id) || 0;
      const nextId = lastId + 1;

      if (prompts.length) {
        setPrompts([
          ...prompts,
          {
            id: nextId,
            value: inputText,
            type: "prompt",
          },
        ]);
      } else {
        setPrompts([
          {
            id: 1,
            value: inputText,
            type: "prompt",
          },
        ]);
      }

      console.log(`Sending post request:`);
      console.log({
        id: nextId,
        prompt: inputText,
      });

      const response = await axios.post(
        "http://localhost:8080/prompt/send",
        {
          id: nextId,
          prompt: inputText,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      setInputText("");

      console.log("Response:", response.data);

      connect();
    } catch (error) {
      console.error("Error sending POST request:", error);
      setPrompts(prompts.slice(0, -1));
    }
  };

  return (
    <div className="h-screen flex flex-col bg-gray-100">
      {sseDown && (
        <div className="bg-red-500 text-white text-center py-2">
          Real-time connection lost - trying again when you refresh.
        </div>
      )}
      {/* Title */}
      <h4 className="text-4xl font-semibold text-center text-gray-800 py-6">
        Closed AI - Model C4
      </h4>

      {/* Response / Prompt History Area */}
      {/* TODO: https://github.com/cure53/DOMPurify */}
      <div className="flex-1 overflow-y-auto px-4">
        <div className="max-w-2xl mx-auto space-y-4 pt-4">
          {prompts.map((prompt: any, idx: number) => (
            <div
              key={idx}
              className="bg-white p-4 rounded shadow whitespace-pre-wrap"
            >
              {prompt.type === "prompt" ? "Prompt:\n" : "Response:\n"}
              <div className="overflow-x-auto">
                <div
                  className="whitespace-pre-wrap rendered-html"
                  dangerouslySetInnerHTML={{ __html: prompt.value }}
                />
              </div>
            </div>
          ))}
          {prompts[prompts.length - 1]?.type == "prompt" && ( // TODO: Only show loading when response to prompt being send was successful
            <BallTriangle fill="#000000" width={50} height={50} />
          )}
        </div>

        <div ref={divScrollDownRef} />
      </div>

      {/* Prompt */}
      <div className="flex items-start justify-center p-4 pb-30">
        <div className="flex items-start gap-2">
          <textarea
            ref={textareaRef}
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            placeholder="Enter text..."
            onInput={handleInput}
            className="bg-white w-150 max-h-60 px-4 py-2 border border-gray-300 rounded-lg shadow-sm resize-none overflow-y-auto focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <button
            onClick={handleSubmit}
            className="h-10 p-2 mt-1 rounded-md bg-gray-200 hover:bg-gray-300 text-gray-700 hover:text-black transition-colors"
            aria-label="Search"
          >
            <Search className="w-5 h-5" />
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;
