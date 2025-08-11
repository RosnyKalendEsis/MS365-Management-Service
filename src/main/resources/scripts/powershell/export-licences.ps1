$users = Get-AzureADUser -All $true

$result = @()

foreach ($user in $users) {
    $licenses = Get-AzureADUserLicenseDetail -ObjectId $user.ObjectId

    $licenseDetails = @()
    foreach ($license in $licenses) {
        $serviceStatuses = @()

        foreach ($service in $license.ServicePlans) {
            $serviceStatuses += @{
                ServicePlan = @{
                    ServiceName = $service.ServicePlanName
                    ProvisioningStatus = $service.ProvisioningStatus
                }
            }
        }

        $licenseDetails += @{
            AccountSkuId = $license.AccountSkuId
            ServiceStatus = $serviceStatuses
        }
    }

    $result += @{
        DisplayName = $user.DisplayName
        UserPrincipalName = $user.UserPrincipalName
        isLicensed = $licenses.Count -gt 0
        Licenses = $licenseDetails
    }
}

# Convertit en JSON formaté
$result | ConvertTo-Json -Depth 5