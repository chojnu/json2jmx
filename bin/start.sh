#! /bin/sh
base_dir=$(realpath $0)
base_dir=$(dirname $base_dir)
base_dir=$(cd $base_dir/..;pwd)

ARGS="-Xms4096m -Xmx4096m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m "
ARGS=$ARGS"-XX:+UseParNewGC -XX:+UseConcMarkSweepGC "
ARGS=$ARGS"-XX:+CMSParallelInitialMarkEnabled -XX:+CMSParallelRemarkEnabled -XX:+CMSScavengeBeforeRemark "
ARGS=$ARGS"-XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75 "

java $ARGS -cp $base_dir/conf:$base_dir/lib/* $@