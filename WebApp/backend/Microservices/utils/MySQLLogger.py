import os
import mysql.connector
from datetime import datetime
from dotenv import load_dotenv

load_dotenv()

db_config = {
    'host': os.getenv('DB_HOST'),
    'port': os.getenv('DB_PORT'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD'),
    'database': os.getenv('DB_NAME')
}

class MySQLLogger:
    def __init__(self):
        self.connect()

    def connect(self):
        try:
            self.connection = mysql.connector.connect(**db_config)
            self.cursor = self.connection.cursor()
            self.create_table_if_not_exists()
        except mysql.connector.Error as err:
            print(f"Error connecting to MySQL: {err}")
            self.connection = None
            self.cursor = None

    def create_table_if_not_exists(self):
        if self.cursor:
            self.cursor.execute("""
                CREATE TABLE IF NOT EXISTS message_history (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    `channel` VARCHAR(255),
                    `key` VARCHAR(255),
                    `value` TEXT,
                    received_at DATETIME
                )
            """)
            self.connection.commit()

    def insert_message(self, channel, key, value):
        try:
            if not self.connection.is_connected():
                print("Reconnecting to MySQL...")
                self.connect()

            self.cursor.execute("""
                INSERT INTO message_history (`channel`, `key`, `value`, `received_at`)
                VALUES (%s, %s, %s, %s)
            """, (channel, key, value, datetime.now()))
            self.connection.commit()
        except mysql.connector.Error as err:
            print(f"MySQL Insert Error: {err}")
            self.connect()  # attempt reconnect on next call

    def close(self):
        if self.cursor:
            self.cursor.close()
        if self.connection:
            self.connection.close()