#!/bin/bash

# Check if Homebrew is installed
if ! command -v brew &>/dev/null; then
  echo "Error: Homebrew is not installed. Please install Homebrew manually and re-run this script."
  exit 1
else
  echo "Homebrew is already installed."
fi

# Check if GPG is installed
if ! command -v gpg &>/dev/null; then
  echo "GPG is not installed. Installing GPG via Homebrew..."

  # Install GPG using Homebrew
  brew install gpg

  if [ $? -ne 0 ]; then
    echo "Failed to install GPG. Exiting."
    exit 1
  fi

  echo "GPG installed successfully!"
else
  echo "GPG is already installed."
fi

# Instructions for passphrase requirements
echo "-----------------------------------------------------"
echo "Passphrase Requirements:"
echo "1. At least 12 characters long (recommended for security)."
echo "2. Should include uppercase, lowercase, numbers, and special characters."
echo "3. Avoid using easily guessable information (e.g., names, birthdays)."
echo "4. Protect your passphrase: do not share it with anyone."
echo "-----------------------------------------------------"

# Check if the required arguments are provided
if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <email> <passphrase>"
  exit 1
fi

# Accept email and passphrase from command-line arguments
EMAIL="$1"
PASSPHRASE="$2"

# Define other key details
REAL_NAME="Your Name"
COMMENT="Generated via script"
KEY_TYPE="RSA"
KEY_LENGTH="4096"
EXPIRE_DATE="0"

# Print a summary of inputs
echo "Generating a GPG key pair with the following details:"
echo "-----------------------------------------------------"
echo "Name: $REAL_NAME"
echo "Email: $EMAIL"
echo "Comment: $COMMENT"
echo "Key Type: $KEY_TYPE"
echo "Key Length: $KEY_LENGTH"
echo "Expiration Date: $EXPIRE_DATE"
echo "-----------------------------------------------------"

# Create a batch configuration file for GPG
BATCH_FILE="gpg_batch_config"
cat > "$BATCH_FILE" <<EOF
Key-Type: $KEY_TYPE
Key-Length: $KEY_LENGTH
Subkey-Type: $KEY_TYPE
Subkey-Length: $KEY_LENGTH
Name-Real: $REAL_NAME
Name-Email: $EMAIL
Name-Comment: $COMMENT
Expire-Date: $EXPIRE_DATE
Passphrase: $PASSPHRASE
%commit
EOF

# Generate the GPG key pair
gpg --batch --gen-key "$BATCH_FILE"

# Delete the batch configuration file (for security)
rm -f "$BATCH_FILE"

# Export the public key (optional)
OUTPUT_PUBLIC_KEY="public-key.asc"
gpg --export --armor "$EMAIL" > "$OUTPUT_PUBLIC_KEY"
echo "Public key exported to $OUTPUT_PUBLIC_KEY"

# Export the private key (optional - backup only, handle with care!)
OUTPUT_PRIVATE_KEY="private-key.asc"
gpg --export-secret-keys --armor "$EMAIL" > "$OUTPUT_PRIVATE_KEY"
echo "Private key exported to $OUTPUT_PRIVATE_KEY"

echo "GPG key generation completed successfully!"