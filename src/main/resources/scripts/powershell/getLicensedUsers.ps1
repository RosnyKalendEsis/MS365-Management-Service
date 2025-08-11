$targetEmails = @("18ka108@esisalama.org")
$result = @()

foreach ($email in $targetEmails) {
    $user = Get-AzureADUser -Filter "UserPrincipalName eq '$email'"
    if ($null -eq $user) { continue }

    $userLicenses = Get-AzureADUserLicenseDetail -ObjectId $user.ObjectId
    $licensesInfo = @()

    foreach ($license in $userLicenses) {
        $services = @()
        foreach ($service in $license.ServicePlans) {
            $services += @{
                ServicePlan = @{
                    ServiceName = $service.ServicePlanName
                    ProvisioningStatus = $service.ProvisioningStatus
                }
            }
        }

        $licensesInfo += @{
            AccountSkuId = $license.AccountSkuId
            ServiceStatus = $services
        }
    }

    $result += @{
        DisplayName = $user.DisplayName
        UserPrincipalName = $user.UserPrincipalName
        isLicensed = ($userLicenses.Count -gt 0)
        Licenses = $licensesInfo
    }
}

$result | ConvertTo-Json -Depth 5