Brendan King

To build:
docker build . -f BootstrapDockerfile -t prj5bootstrap
docker build . -f PeerDockerfile -t prj5peer
docker build . -f ClientDockerfile -t prj5client

Or:
docker build . -f BootstrapDockerfile -t prj5bootstrap && docker build . -f PeerDockerfile -t prj5peer && docker build . -f ClientDockerfile -t prj5client

Run the test files using:
docker compose -f testcases/testcase-[TESTCASE_NUM]-prj5 up
