## ProfilePatch
Small patch for authlib, adding cache for non-existent player UUIDs.
For example, placing a lot player heads of one non-existent name can create repeated huge lags on the server, and with this patch, it will only lag at the first time.  