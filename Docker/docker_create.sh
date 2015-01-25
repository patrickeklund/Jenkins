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
    echo "  -all     :  builds all docker images in proper order"
    echo "  -jbase   :  builds Jenkins Base docker image"
    echo "  -jCustom :  builds Jenkins Custmom docker image"
    echo "  -centos  :  builds centos docker image"
    exit
}


build_dockerImage() {
    printf "Building %-30s from %10s\n" "$1" "$2"
    docker build -t="$1" $2
}


# Check that only 1 argument is given #
if [ $# -ne 1 ] ; then
    usage $#
fi

case "$1" in

-all)       build_dockerImage    local/jenkins:1.594          base/
            build_dockerImage    local/jenkins:1.594_custom   custom/
            build_dockerImage    local:apache                 centOs/
            ;;
-jbase)     build_dockerImage    local/jenkins:1.594          base/
            ;;
-jcustom)   build_dockerImage    local/jenkins:1.594          base/
            build_dockerImage    local/jenkins:1.594_custom   custom/
            ;;
-centos)    build_dockerImage    local/apache:tomcat          centOs/
            ;;
*)          usage $#
            ;;
esac
