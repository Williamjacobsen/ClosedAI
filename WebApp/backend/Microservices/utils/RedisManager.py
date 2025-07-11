import redis
import json

# TODO: ADD OPTION FOR LOGGING (AND LOGGING LEVELS)

class RedisManager:
    """
    A simple Redis manager for publishing and subscribing to channels using Redis Pub/Sub.

    Attributes:
        redis_client (redis.Redis): Redis client for publishing messages.
        pubsub (redis.client.PubSub): Redis PubSub object for subscribing to channels.
    """

    def __init__(self):
        """
        Initializes the RedisManager by setting up the Redis client and PubSub.
        Assumes Redis is running on localhost and default port 6379.
        """
        self.redis_client = redis.Redis(host='localhost', port=6379, decode_responses=True)
        self.pubsub = self.redis_client.pubsub()

    def redis_subscriber(self, *channels, callback=None):
        """
        Subscribes to the specified Redis channels and listens for messages.
        Calls the optional callback function with the message channel, key, and value.

        Args:
            *channels: One or more Redis channel names to subscribe to.
            callback (function, optional): Function to call when a message is received.
                                        Should accept three arguments: channel, key, value.

        Raises:
            ValueError: If no channels are provided.
        """
        try:
            if not channels:
                raise ValueError("At least one channel must be provided")

            self.pubsub.subscribe(*channels)
            print(f"Subscribed to channels: {', '.join(channels)}")

            for message in self.pubsub.listen():
                if message['type'] == 'message':
                    try:
                        data = json.loads(message['data'])
                        key = data.get('key')
                        value = data.get('value')
                        channel = message['channel']

                        print(f"Received on {channel}:")
                        print(f"   Key    : {key}")
                        print(f"   Value  : {value}")

                        if callback:
                            callback(channel, key, value)

                    except json.JSONDecodeError:
                        print("Received non-JSON message:", message['data'])

        except Exception as e:
            print(f"Error subscribing to {', '.join(channels)}: {e}")

    def redis_publisher(self, channel, message):
        """
        Publishes a message to the specified Redis channel.

        Args:
            channel (str): The Redis channel to publish to.
            message (str or dict): The message to publish. If a dictionary is provided,
                                   it will be converted to a JSON string.

        Raises:
            Exception: If publishing fails.
        """
        try:
            if isinstance(message, dict):
                message = json.dumps(message)

            self.redis_client.publish(channel, message)
            print(f"Published to '{channel}': {message}")

        except Exception as e:
            print(f"Error publishing to {channel}: {e}")
