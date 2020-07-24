package com.sample.domain;

public class AccountRepositoryImpl {
//
//    private RedisTemplate<String, Account> redisTemplate;
//
//    private final HashOperations<String, String, Account> hashOperations;
//
//    public AccountRepositoryImpl(RedisTemplate<String, Account> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.hashOperations = redisTemplate.opsForHash();
//    }
//
//    public void save(Account account) {
//        hashOperations.put("ACCOUNT", account.getId(), account);
//    }
//
//    public Map<String, Account> findAll() {
//        return hashOperations.entries("ACCOUNT");
//    }
//
//    public Account findById(String id) {
//        return (Account) hashOperations.get("ACCOUNT", id);
//    }
//
//    public void update(Account account) {
//        save(account);
//    }
//
//    public void delete(String id) {
//        hashOperations.delete("ACCOUNT", id);
//    }
}
