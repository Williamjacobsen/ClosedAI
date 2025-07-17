export type SSEOptions = {
  url: string;
  onMessage: (event: MessageEvent) => void;
  eventName: string;
  onOpen?: () => void;
  onError?: (event: Event) => void;
  onClose?: () => void;
  withCredentials?: boolean;
};

export function createSSEConnection({
  url,
  onMessage,
  eventName,
  onOpen,
  onError,
  onClose,
  withCredentials,
}: SSEOptions): EventSource {
  const eventSource = new EventSource(url, { withCredentials });

  eventSource.addEventListener(eventName, onMessage);

  if (onOpen) eventSource.onopen = onOpen;

  if (onError) {
    eventSource.onerror = (event) => {
      onError(event);
      eventSource.close();
    };
  }

  if (onClose) {
    eventSource.addEventListener("close", () => {
      onClose();
      eventSource.close();
    });
  }

  return eventSource;
}
