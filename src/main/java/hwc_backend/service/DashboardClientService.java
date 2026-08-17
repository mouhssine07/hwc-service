package hwc_backend.service;

import hwc_backend.dto.dashboard.DashboardClientDTO;

public interface DashboardClientService {

    DashboardClientDTO getDashboard(String email);
}
