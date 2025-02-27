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
*/secrets/* filter=git-crypt diff=git-crypt
EOL

echo ".gitattributes file updated with encryption rules."

# Step 4: Create example sensitive files
echo "Creating example sensitive files based on .gitattributes rules..."
mkdir -p secrets
echo "This is a secret config file." > config.secret
echo "API_KEY=super-secret-api-key" > secrets/api_key.txt

echo "Example files 'config.secret' and 'secrets/api_key.txt' created."

# Step 5: Verify .gitattributes rules are applied
echo "Verifying .gitattributes rules..."
git check-attr filter config.secret
git check-attr filter secrets/api_key.txt

# Step 6: Add sensitive files to Git
echo "Adding sensitive files to Git..."
git add config.secret secrets/api_key.txt

# Step 7: Commit changes
echo "Committing sensitive files with encryption..."
git commit -m "Add sensitive files with git-crypt encryption rules"

# Step 8: Display final instructions
echo "Sensitive files are now encrypted and tracked in the repository!"
echo "What you need to do next:"
echo "1. Authorize users with access to encrypted files using their GPG key:"
echo "   git-crypt add-gpg-user [username_or_email]"
echo "2. Push your changes to your Git repository (e.g., 'git push')."
echo "3. Test encryption by checking contents in your remote repository or the .git directory."
echo "4. Collaborators can unlock encrypted files using: git-crypt unlock"
echo "   (Ensure they have been granted access and have the decryption key)."
