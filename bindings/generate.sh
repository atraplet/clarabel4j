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
HEADER_FILE=include/clarabel.h

# Define variables
ARTIFACT_ID=com.ustermetrics."${PROJECT_NAME}"
ARTIFACT_ID_DIR=$(echo "${ARTIFACT_ID}" | sed 's/\./\//g')

TMP_DIR=$(dirname "$(mktemp -u)")
REPO_DIR="${TMP_DIR}"/"${REPO##*/}"
HEADER_FILE_FULL_PATH="${REPO_DIR}"/"${HEADER_FILE}"

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
  echo "OS ${OSTYPE} not supported"
  exit 1
fi

# Clone and checkout repo
echo "Clone repository ${REPO} into ${REPO_DIR}"
rm -rf "${REPO_DIR}"
cd "${TMP_DIR}" || { echo "Error: Failed to change directory to ${TMP_DIR}"; exit 1; }
git clone "${REPO}" || { echo "Error: Failed to clone repository ${REPO}"; exit 1; }
cd "${REPO_DIR}" || { echo "Error: Failed to change directory to ${REPO_DIR}"; exit 1; }
git checkout "${VERSION}" || { echo "Error: Failed to checkout version ${VERSION}"; exit 1; }

# Apply patches
if [ -d "${PATCHES_DIR}" ]; then
  if ls "${PATCHES_DIR}"/*.patch 1> /dev/null 2>&1; then
    PATCHES=$(ls "${PATCHES_DIR}"/*.patch)
    echo "Apply patches ${PATCHES}"
    git apply "${PATCHES_DIR}"/*.patch
  fi
fi

if [ "${DUMP_INCLUDES}" = "true" ]; then
  echo "Dump symbols"

  # Dump included symbols
  "${JEXTRACT}" \
    --define-macro FEATURE_FAER_SPARSE \
    --define-macro FEATURE_PARDISO_MKL \
    --define-macro FEATURE_PARDISO_ANY \
    --dump-includes "${INCLUDES_FILE}" \
    "${HEADER_FILE_FULL_PATH}" || { echo "Error: Failed to dump symbols"; exit 1; }

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

  echo "Generate bindings"

  # Remove old bindings
  rm -rf "${JAVA_SRC_DIR}"/"${ARTIFACT_ID_DIR}"/bindings

  # Generate bindings
  "${JEXTRACT}" \
    --define-macro FEATURE_FAER_SPARSE \
    --define-macro FEATURE_PARDISO_MKL \
    --define-macro FEATURE_PARDISO_ANY \
    --target-package "${ARTIFACT_ID}".bindings \
    --output "${JAVA_SRC_DIR}" \
    @"${BINDINGS_DIR}"/includes.txt "${HEADER_FILE_FULL_PATH}" || { echo "Error: Failed to generate bindings"; exit 1; }

fi

# Cleanup
rm -rf "${REPO_DIR}"
