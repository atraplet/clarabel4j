#! /bin/bash

# Usage
USAGE="\
Usage: generate [--dump-includes]"

# Read command line arguments
if [ $# -eq 0 ] ; then
  DUMP_INCLUDES=false
elif [ $# -eq 1 ] && [ "${1}" = "--dump-includes" ]; then
  DUMP_INCLUDES=true
else
  echo "$USAGE"
  exit 1
fi

# Define project name, version, repo, and header file
PROJECT_NAME=clarabel4j
VERSION=v0.11.1
REPO="https://github.com/oxfordcontrol/Clarabel.cpp"
HEADER_FILE=include/Clarabel.h

# Define variables
ARTIFACT_ID=com.ustermetrics."${PROJECT_NAME}"
ARTIFACT_ID_DIR=$(echo "${ARTIFACT_ID}" | sed 's/\./\//g')

TMP_DIR=$(dirname "$(mktemp -u)")
REPO_DIR="${TMP_DIR}"/"${REPO##*/}"
HEADER_FILE="${REPO_DIR}"/"${HEADER_FILE}"

BINDINGS_DIR=$(dirname "$(realpath "${0}")")
PROJECT_ROOT="${BINDINGS_DIR}"/..
PATCHES_DIR="${PROJECT_ROOT}"/patches
INCLUDES_FILE="${BINDINGS_DIR}"/includes.txt
JAVA_SRC_DIR="${PROJECT_ROOT}"/src/main/java

if [[ "$OSTYPE" = "linux-gnu"* ]]; then
  JEXTRACT=jextract
elif [[ "$OSTYPE" = "msys"* ]]; then
  JEXTRACT=jextract.bat
else
  echo "OS not supported"
  exit 1
fi

# Clone and checkout repo
rm -rf "${REPO_DIR}"
cd "${TMP_DIR}" || exit 1
git clone "${REPO}"
cd "${REPO_DIR}" || exit 1
git checkout "${VERSION}"

# Evt. apply patches
if [ -d "${PATCHES_DIR}" ]; then
  git apply "${PATCHES_DIR}"/*.patch
fi

if [ "${DUMP_INCLUDES}" = "true" ]; then

  # Dump included symbols
  "${JEXTRACT}" \
    --define-macro FEATURE_FAER_SPARSE \
    --define-macro FEATURE_PARDISO_MKL \
    --define-macro FEATURE_PARDISO_ANY \
    --dump-includes "${INCLUDES_FILE}" \
    "${HEADER_FILE}"

  # Select symbols
  grep "Clarabel" "${INCLUDES_FILE}" \
    | grep -v "f32" \
    | grep -v "\-\-include\-typedef ClarabelCscMatrix " \
    | grep -v "\-\-include\-typedef ClarabelDefaultInfo " \
    | grep -v "\-\-include\-typedef ClarabelDefaultSettings " \
    | grep -v "\-\-include\-typedef ClarabelDefaultSolution " \
    | grep -v "\-\-include\-typedef ClarabelDefaultSolver " \
    | grep -v "\-\-include\-function clarabel_DefaultSolver_f64_set_termination_callback " \
    | grep -v "\-\-include\-function clarabel_DefaultSolver_f64_unset_termination_callback " \
    | grep -v "\-\-include\-typedef ClarabelCallbackFcn " \
    | grep -v "\-\-include\-typedef ClarabelCallbackFcn_f64 " \
    | grep -v "\-\-include\-typedef ClarabelSupportedConeT " >"${INCLUDES_FILE}".tmp && mv "${INCLUDES_FILE}".tmp "${INCLUDES_FILE}"

else

  # Remove old bindings
  rm -rf "${JAVA_SRC_DIR}"/"${ARTIFACT_ID_DIR}"/bindings

  # Generate bindings
  "${JEXTRACT}" \
    --define-macro FEATURE_FAER_SPARSE \
    --define-macro FEATURE_PARDISO_MKL \
    --define-macro FEATURE_PARDISO_ANY \
    --target-package "${ARTIFACT_ID}".bindings \
    --output "${JAVA_SRC_DIR}" \
    @"${BINDINGS_DIR}"/includes.txt "${HEADER_FILE}"

fi

# Cleanup
rm -rf "${REPO_DIR}"
