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
    echo "  -all     :  stop all docker images"
    echo "  -jbase   :  stop Jenkins Base docker image"
    echo "  -jCustom :  stop Jenkins Custmom docker image"
    echo "  -centos  :  stop centos docker image"
    exit
}


remove_dockerImage() {

    if [ $(docker ps -a | grep $1 | awk {'print $1'}) ] ; then
        echo Remove any [$1] running container forcefully, stopping it when needed
        docker rm -f $(docker ps -a | grep $1 | awk {'print $1'})
    fi
    

    if [ $(docker images --filter 'image=$1') ] ; then
       echo Delete all all images related to [$1]
       docker rmi $(docker images --filter 'image=$1')
    else
       echo No images to delete related to [$1]
    fi
}


remove_allDockerImage() {

    if [ $(docker ps -a | grep -v CONTAINER | awk {'print $1'}) ] ; then
       echo Remove all running containers forcefully, stopping them when needed
       docker rm -f $(docker ps -a -q)    
    fi

    if [ ”$(docker images -q)” == ”null” ] ; then
       echo Delete all all images
       docker rmi $(docker images -q)
    else
       echo No images to delete
    fi
}


# Check that only 1 argument is given #
if [ $# -ne 1 ] ; then
    usage $#
fi

case "$1" in

-all)       remove_allDockerImage
            ;;
-jbase)     remove_dockerImage    local/jenkins:1.594
            ;;
-jcustom)   remove_dockerImage    local/jenkins:1.594_custom
            ;;
-centos)    remove_dockerImage    local/apache:tomcat
            ;;
*)          usage $#
            ;;
esac

