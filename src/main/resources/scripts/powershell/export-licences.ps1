# test-get-user.ps1

# Import du module AzureAD (si pas auto-importé)
Import-Module AzureAD

# Connexion automatique si possible (ou token pré-enregistré)
# Attention : cette commande nécessite une session interactive, donc pour test local uniquement
# Connect-AzureAD

# Récupération d'un utilisateur (remplacer par un user réel ou utiliser un filtre)
Get-AzureADUser -Top 1 | ConvertTo-Json -Depth 3