

# Run nginx in foreground.
daemon off;

# This is run inside Docker.
user nginx;

# Pid storage location.
pid /var/run/nginx.pid;

# Set number of worker processes.
worker_processes 1;

# Enables the use of JIT for regular expressions to speed-up their processing.
pcre_jit on;

# Write error log to the add-on log.
error_log /proc/1/fd/1 error;

# Max num of simultaneous connections by a worker process.
events {
    worker_connections 512;
}

http {
    default_type            application/octet-stream;
    access_log              off;
    sendfile                on;
    tcp_nopush              on;

    server {
        listen 8099 default_server;
        server_name _;

        location / {
            allow   172.30.32.2;
            deny    all;

            set     $target "{{ .server }}";

            proxy_pass                  $target;

            proxy_set_header Accept-Encoding "";
            proxy_set_header Connection $connection_upgrade;
            proxy_set_header Host $http_host;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_set_header X-NginX-Proxy true;
            proxy_set_header X-Real-IP $remote_addr;
        }
    }
}


