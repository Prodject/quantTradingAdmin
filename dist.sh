
# sbt dist
# tar  target/universal/viewfinquanttradingai-1.0-SNAPSHOT.tgz  -d production


sbt universal:packageZipTarball
rm -rf  production
mkdir production
tar -xf target/universal/viewfinquanttradingai-1.0-SNAPSHOT.tgz -C production
 