#! /bin/bash

TEST_CMD='./gradlew test --info -Dcom.github.drstefanfriedrich.f2blib.performancetest.enabled=true --tests com.github.drstefanfriedrich.f2blib'
WARMUP=3
REPETITIONS=100

# Compile the whole project
./gradlew
./gradlew --stop
sleep 2

# Prepare the log file
rm performance-test.log
touch performance-test.log

# Function definition
function performance_test() {
    local PACKAGE_NAME=$1
    if [[ ! -z "${PACKAGE_NAME}" ]]
    then
        PACKAGE_NAME=${PACKAGE_NAME}.
    fi
    local CLASS_NAME=$2
    echo "Executing performance test com.github.drstefanfriedrich.f2blib.${PACKAGE_NAME}${CLASS_NAME}.performance"
    echo ""
    # Warm-up
    for i in $(seq 1 ${WARMUP})
    do
        ${TEST_CMD}.${PACKAGE_NAME}${CLASS_NAME}.performance
        ./gradlew --stop
        sleep 2
    done
    # Performance measurement
    rm ${CLASS_NAME}.performance.log
    touch ${CLASS_NAME}.performance.log
    for i in $(seq 1 ${REPETITIONS})
    do
        ${TEST_CMD}.${PACKAGE_NAME}${CLASS_NAME}.performance >> ${CLASS_NAME}.performance.log
        ./gradlew --stop
        sleep 5
    done
    # Write log file
    echo "${PACKAGE_NAME}${CLASS_NAME}.performance:" >> performance-test.log
    cat ${CLASS_NAME}.performance.log | grep "Performance should always be better. That's why we fail the unit test." >> performance-test.log
    echo "" >> performance-test.log
    echo ""
}


#
# BytecodeVisitorImplTest.performance
#
performance_test visitor BytecodeVisitorImplTest

#
# EvalVisitorImplTest.performance
#
performance_test visitor EvalVisitorImplTest

#
# BytecodePerformanceTest.performance
#
performance_test "" BytecodePerformanceTest

#
# EvalPerformanceTest.performance
#
performance_test "" EvalPerformanceTest

#
# BytecodeLifeInsuranceVariantsTest.performance
#
performance_test lifeinsurance BytecodeLifeInsuranceVariantsTest

#
# EvalLifeInsuranceVariantsTest.performance
#
performance_test lifeinsurance EvalLifeInsuranceVariantsTest

