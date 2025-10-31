## ProfilePatch
Small patch for authlib, adding cache for non-existent player UUIDs.
For example, placing a lot player heads of one non-existent name can create lags on the server on every placement. With this patch, it only lags on the first placement (until the cache have expired).  