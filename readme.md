# Java chat server implementation
TCP based server for chat application. 

# Docs
## Message structure
There is one base format for request: "sender;receiver;message".

## Notifications
Server sends notification if anyone logs in or out, and also sends list of active user on login.
Notifications have following format: "SERVER-receiver;type;message".
There are three types of notification:
* NEW_LOGIN
* NEW_LOGOUT
* USER_LIST

## Logging in
* Open socket with server ip and port
* First string sent to the server will be your username
* After sending username you'll get 2 notifications:
    - user list
    - information about your login
    
## Keep alive
Every 5 seconds server checks if connection is still active through sending "keep_alive" string.
You should ignore it.


