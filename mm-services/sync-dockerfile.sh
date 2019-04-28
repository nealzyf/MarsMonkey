#!/bin/sh


LIST=`find . -name "Dockerfile" -type f`

echo $LIST

for file in $LIST ; do
    cat Dockerfile.template > ${file}
    echo "Updated : ${file}"
done

exit 0