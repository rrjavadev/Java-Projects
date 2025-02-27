#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Step 1: Check for git-crypt installation
if command_exists git-crypt; then
    echo "git-crypt is already installed."
else
    echo "git-crypt is not installed. Installing..."

    # Check OS and install git-crypt
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        if command_exists brew; then
            brew install git-crypt
        else
            echo "Homebrew is not installed. Please install Homebrew first: https://brew.sh"
            exit 1
        fi
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        if command_exists apt-get; then
            sudo apt-get update
            sudo apt-get install -y git-crypt
        else
            echo "apt-get is not available. Please install git-crypt manually."
            exit 1
        fi
    else
        echo "Unsupported OS. Please install git-crypt manually: https://github.com/AGWA/git-crypt"
        exit 1
    fi
fi

# Step 2: Initialize git-crypt in the repository
if [[ -d ".git" ]]; then
    echo "Initializing git-crypt in this repository..."
    git-crypt init
else
    echo "This is not a Git repository. Please run 'git init' first."
    exit 1
fi

# Step 3: Create or update .gitattributes file
GITATTRIBUTES_FILE=".gitattributes"

if [[ -f "$GITATTRIBUTES_FILE" ]]; then
    echo "Found existing .gitattributes file. Adding default encryption rules..."
else
    echo "Creating .gitattributes file with default encryption rules..."
fi

cat <<EOL >> "$GITATTRIBUTES_FILE"

# Rules for encrypting sensitive files
*.secret filter=git-crypt diff=git-crypt
secrets/* filter=git-crypt diff=git-crypt
EOL

echo ".gitattributes file updated with encryption rules."

# Prompt: Ask user whether to create a single file or a folder of secrets
echo "Do you want to create a single secrets file or a secrets folder?"
echo "1) Single file"
echo "2) Folder of secrets"
read -p "Enter your choice (1/2): " choice

# Step 4: Create secrets file or folder based on user input
mkdir -p secrets

if [[ "$choice" == "1" ]]; then
    # User selected a single file
    read -p "Enter the name of your secrets file (e.g., config.secret): " secret_file
    if [[ -z "$secret_file" ]]; then
        echo "No file name entered. Using default name 'config.secret'."
        secret_file="config.secret"
    fi
    echo "Creating secrets file: secrets/$secret_file ..."
    echo "This is a sensitive secret file." > "secrets/$secret_file"
elif [[ "$choice" == "2" ]]; then
    # User selected a folder
    echo "Creating a secrets folder with example files..."
    echo "API_KEY=super-secret-api-key" > "secrets/api_key.txt"
    echo "PASSWORD=super-secret-password" > "secrets/password.txt"
    echo "Secrets folder created with 'api_key.txt' and 'password.txt'."
else
    echo "Invalid choice. Exiting without creating secrets."
    exit 1
fi

# Step 5: Verify .gitattributes rules are applied
echo "Verifying .gitattributes rules..."
if [[ "$choice" == "1" ]]; then
    git check-attr filter "secrets/$secret_file"
fi
git check-attr filter secrets/api_key.txt 2>/dev/null
git check-attr filter secrets/password.txt 2>/dev/null

# Step 6: Add sensitive files to Git
echo "Adding sensitive files to Git..."
if [[ "$choice" == "1" ]]; then
    git add "secrets/$secret_file"
else
    git add secrets/api_key.txt secrets/password.txt
fi

# Step 7: Commit changes
echo "Committing sensitive files with encryption..."
if [[ "$choice" == "1" ]]; then
    git commit -m "Add secrets file ($secret_file) with git-crypt encryption"
else
    git commit -m "Add secrets folder with example files and git-crypt encryption"
fi

# Step 8: Display final instructions
echo "Sensitive files are now encrypted and tracked in the repository!"
echo "What you need to do next:"
echo "1. Authorize users with access to encrypted files using their GPG key:"
echo "   git-crypt add-gpg-user [username_or_email]"
echo "2. Push your changes to your Git repository (e.g., 'git push')."
echo "3. Test encryption by checking contents in your remote repository or the .git directory."
echo "4. Collaborators can unlock encrypted files using: git-crypt unlock"
echo "   (Ensure they have been granted access and have the decryption key)."