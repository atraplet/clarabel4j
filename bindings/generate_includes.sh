#! /bin/bash

# main
USAGE="\
Usage: generate_includes path_to_clarabel_source_package path_to_jextract_binary"

# read command line arguments
if [ $# -eq 2 ]; then
  CLARABEL4J=$(dirname "${0}")/../
  CLARABEL="${1}"
  JEXTRACT="${2}"
else
  echo "$USAGE"
  exit 1
fi

# define variables
TMP_INCLUDES="${CLARABEL4J}"/bindings/tmp_includes.txt
INCLUDES="${CLARABEL4J}"/bindings/includes.txt

# dump included symbols
rm -f "${TMP_INCLUDES}"
rm -f "${INCLUDES}"
${JEXTRACT} \
  --dump-includes "${TMP_INCLUDES}" \
  "${CLARABEL}"/include/Clarabel.h

# select symbols
#grep "ecos\|fflush" "${TMP_INCLUDES}" | grep -v "SuiteSparse\|timer\|fflush_nolock\|fflush_unlocked" >"${INCLUDES}"
#rm -f "${TMP_INCLUDES}"
