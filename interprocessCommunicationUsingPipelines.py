import os
import sys

def child_process(read_fd, write_fd):
    os.close(write_fd)
    with os.fdopen(read_fd, 'r') as read_end:
        print("Child: Waiting for message..")
        message = read_end.read()
        print(f"Child: received message: {message}")

if __name__ == '__main__':
    read_fd, write_fd = os.pipe()

    try:
        pid = os.fork()
    except AttributeError:
        sys.exit("os.fork() is not available on this platform (Windows). Run on Unix-like OS.")

    if pid > 0:
        os.close(read_fd)
        with os.fdopen(write_fd, 'w') as write_end:
            message_to_send = "Message from parent to child"
            print(f"Parent: Sending message: {message_to_send}")
            write_end.write(message_to_send)
            write_end.flush()
        _, status = os.waitpid(pid, 0)
        print(f"Parent: child process finished with status {status}")
    else:
        child_process(read_fd, write_fd)
        os._exit(0)
