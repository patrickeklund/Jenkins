#!/bin/bash

# describe usage

usage () {

    echo

    if   [ $1 -lt 1 ] ; then
        echo "$1 < 1 : To few arguments given"
    elif [ $1 -gt 1 ] ; then
        echo "$1 > 1 : To many arguments given"
    else
        echo "Invalid option"
    fi 

    echo
    echo "Usage:"
    echo "  -all     :  remove all docker images"
    echo "  -jbase   :  remove Jenkins Base docker image"
    echo "  -jCustom :  remove Jenkins Custmom docker image"
    echo "  -centos  :  remove centos docker image"
    exit
}


remove_dockerImage() {

    if [ $(docker ps -a | grep $1:$2 | awk {'print $1'}) ] ; then
        echo Remove any [$1:$2] running container forcefully, stopping it when needed
        docker rm -f $(docker ps -a | grep $1:$2 | awk {'print $1'})
    fi
    
    if [ $(docker images -a | grep $1 | grep $2 | awk {'print $3'} ) ] ; then
       echo Remove all all images related to [$1:$2]
       docker rmi $(docker images -a | grep $1 | grep $2 | awk {'print $3'})
    else
       echo No images present related to [$1:$2]
    fi
}


remove_allDockerImage() {

    if [ $(docker ps -a | grep -v CONTAINER | awk {'print $1'}) ] ; then
       echo Remove all running containers forcefully, stopping them when needed
       docker rm -f $(docker ps -a -q)    
    fi

    var=$(docker images -q)
    if [ ”$var” != ”” ] ; then
       echo Remove all all images
       docker rmi $(docker images -q)
    else
       echo No images present
    fi
}


# Check that only 1 argument is given #
if [ $# -ne 1 ] ; then
    usage $#
fi

case "$1" in

-all)       remove_allDockerImage
            ;;
-jbase)     remove_dockerImage    local/jenkins   1.594
            ;;
-jcustom)   remove_dockerImage    local/jenkins   1.594_custom
            ;;
-centos)    remove_dockerImage    local/apache    tomcat
            ;;
*)          usage $#
            ;;
esac

