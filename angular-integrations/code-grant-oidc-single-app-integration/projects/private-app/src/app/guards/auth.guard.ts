import { CanActivateFn } from "@angular/router";
import { inject } from "@angular/core";
import { StartupService } from "../service/startup.service";

export const authGuard: CanActivateFn = async (route, state) => {
    const startupService = inject(StartupService);
    if (startupService.loginUser) {
        return true;
    }
    window.location.href =
        window.location.origin +
        "/api/private-app/actions/authenticate?redirectPath=/pages/private-app" +
        state.url;
    return false;
};
