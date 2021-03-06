package com.queue.auth.core;

import io.reactivex.Single;

public interface MetaDataStorage {
  Single<User> getUser(AuthRequest request);
}
