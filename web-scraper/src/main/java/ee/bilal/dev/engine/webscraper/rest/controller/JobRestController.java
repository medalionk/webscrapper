//package ee.bilal.dev.engine.webscraper.rest.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Created by bilal90 on 9/6/2017.
// */
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/portal")
//public class JobRestController extends BaseRestController<AccountDTO> {
//    private final AccountServiceImpl accountService;
//    private final ObjectMapper mapper;
//
//    @Autowired
//    public JobRestController(AccountServiceImpl accountService, @Qualifier("objectMapper") ObjectMapper mapper) {
//        super(AccountRestController.class, accountService);
//        this.accountService = accountService;
//        this.mapper = mapper;
//    }
//
//    @GetMapping("/auth")
//    public ResponseEntity<SlimAccountDTO> auth(HttpServletRequest req) {
//        return ResponseEntity.ok(accountService.auth(req));
//    }
//
//    @GetMapping("/agencies/{agencyId}/accounts")
//    public ResponseEntity<List<AccountDTO>> getAll(@PathVariable("agencyId") String agencyId) {
//        List<AccountDTO> accounts = accountService.findAccountsByAgencyId(agencyId);
//        return ResponseEntity.ok(accounts);
//    }
//
//    @GetMapping("/agencies/{agencyId}/accounts/drivers")
//    public ResponseEntity<List<AccountDTO>> getDriverAccounts(@PathVariable("agencyId") String agencyId) {
//        List<AccountDTO> accounts = accountService.getDriverAccounts(agencyId);
//        return ResponseEntity.ok(accounts);
//    }
//
//    @GetMapping("/agencies/{agencyId}/accounts/{id}")
//    public ResponseEntity<AccountDTO> get(@PathVariable("agencyId") String agencyId, @PathVariable("id") String id) {
//        Optional<AccountDTO> account = accountService.findAccountByIdAndAgencyId(id, agencyId);
//        return ResponseUtil.wrapOrNotFound(account);
//    }
//
//    @PostMapping("/accounts")
//    public ResponseEntity<AccountDTO> create(
//            HttpServletRequest request,
//            @RequestPart(value = "picture", required = false) MultipartFile picture,
//            @RequestParam("account") String accountStr) throws IOException {
//
//        AccountDTO account = mapStringToAccount(accountStr);
//        return ResponseUtil.wrapOrNotFound(accountService.create(request, account, picture));
//    }
//
//    @PatchMapping("/agencies/{agencyId}/accounts/{id}")
//    public ResponseEntity<AccountDTO> update(
//            HttpServletRequest request,
//            @PathVariable(name = "agencyId") String agencyId,
//            @PathVariable(name = "id") String id,
//            @RequestPart(value = "picture", required = false) MultipartFile picture,
//            @RequestParam("account") String accountStr) throws IOException{
//
//        AccountDTO account = mapStringToAccount(accountStr);
//        return ResponseUtil.wrapOrNotFound(accountService.update(id, request, account, picture));
//    }
//
//    @GetMapping("/agencies/{agencyId}/accounts/{id}/activate")
//    public ResponseEntity<AccountDTO> activate(
//            HttpServletRequest req, @PathVariable("agencyId") String agencyId, @PathVariable("id") String id) {
//        Optional<AccountDTO> account = accountService.activate(req, agencyId, id);
//        return ResponseUtil.wrapOrNotFound(account);
//    }
//
//    @GetMapping("/agencies/{agencyId}/accounts/{id}/deactivate")
//    public ResponseEntity<AccountDTO> deactivate(
//            HttpServletRequest req, @PathVariable("agencyId") String agencyId, @PathVariable("id") String id) {
//        Optional<AccountDTO> account = accountService.deactivate(req, agencyId, id);
//        return ResponseUtil.wrapOrNotFound(account);
//    }
//
//    @DeleteMapping("/agencies/{agencyId}/accounts/{id}")
//    public ResponseEntity<Void> delete(@PathVariable("agencyId") String agencyId, @PathVariable("id") String id) {
//        return super.delete(id);
//    }
//
//    private AccountDTO mapStringToAccount(String accountStr)throws IOException {
//        ValidationUtil.validatePropertyNotNullOrEmpty(accountStr, "Account Object As String");
//
//        AccountDTO account = mapper.readValue(accountStr, new TypeReference<AccountDTO>() {});
//        ValidationUtil.validatePropertyNotNull(account, "Account object");
//
//        return account;
//    }
//}