import sys
sys.path.append('../')
from utils.RedisManager import RedisManager
from utils.MySQLLogger import MySQLLogger

mysql_logger = MySQLLogger()

if not mysql_logger.connection:
    exit(1)

def on_response(channel, key, value):
    print(f"Message on [{channel}]:")
    print(f"Key    : {key}")
    print(f"Value  : {value}")
    mysql_logger.insert_message(channel.removesuffix('_channel'), key, value)

if __name__ == '__main__':
    try:
        redis_client = RedisManager()
        redis_client.redis_subscriber("prompt_channel", "response_channel", callback=on_response)
    except KeyboardInterrupt:
        print("Shutting down...")
    finally:
        mysql_logger.close()
