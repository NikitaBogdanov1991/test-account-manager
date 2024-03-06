package ru.effectivemobile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.effectivemobile.model.Account;
import ru.effectivemobile.model.AccountStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
public class AccountsController {
    @GetMapping("/")
    public String chooseARoleForm() {
        return "choose-a-role-form";
    }
    @GetMapping("/choose-a-role-form")
    public String returnToAChooseRoleForm() {
        return "choose-a-role-form";
    }
    @GetMapping("/accounts-list")
    public String accountsList(Model model) {
        model.addAttribute("accounts", AccountStorage.getAccounts());
        return "accounts-list";
    }
    @GetMapping("/create-form")
    public String createForm() {
        return "create-form";
    }
    @PostMapping("/create")
    public String create(Account account) {
        account.setId(UUID.randomUUID().toString());
        AccountStorage.getAccounts().add(account);
        return "redirect:/accounts-list";
    }
    @GetMapping("/edit-manager-form/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Account accountToEdit = AccountStorage.getAccounts().stream().
                filter(account -> account.getId().equals(id)).
                findFirst().orElseThrow(RuntimeException::new);
        model.addAttribute("account", accountToEdit);
        return "edit-manager-form";
    }
    @PostMapping("/update")
    public String update(Account account) {
        delete(account.getId());
        AccountStorage.getAccounts().add(account);
        return "redirect:/accounts-list";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        Account accountToDelete = AccountStorage.getAccounts().stream().
                filter(account -> account.getId().equals(id)).
                findFirst().orElseThrow(RuntimeException::new);
        AccountStorage.getAccounts().remove(accountToDelete);
        return "redirect:/accounts-list";
    }



    @GetMapping("/search-by-phone-number-form")
    public String searchByPhoneNumberForm() {
        return "search-by-phone-number-form";
    }
    @PostMapping("/searchByPhoneNumber")
    public String searchByPhoneNumber(@RequestParam String telephoneNumber,
                        RedirectAttributes redirectAttributes, HttpSession session) {
        Account accountByPhoneNumber = AccountStorage.getAccounts().stream()
                .filter(account -> account.getTelephoneNumber().equals(telephoneNumber))
                .findFirst().orElse(null);
        if (accountByPhoneNumber == null) {
            redirectAttributes.addFlashAttribute("error", "Incorrect telephone number");
            return "redirect:/search-by-phone-number-form";
        }
        session.setAttribute("accountByPhoneNumber", accountByPhoneNumber);
        return "redirect:/account-by-phoneNumber-page";
    }
    @GetMapping("/account-by-phoneNumber-page")
    public String accountByPhoneNumberPage(Model model, HttpSession session) {
        Account accountByPhoneNumber = (Account) session.getAttribute("accountByPhoneNumber");
        if (accountByPhoneNumber != null) {
            model.addAttribute("accountByPhoneNumber", accountByPhoneNumber);
            return "account-by-phoneNumber-page";
        }
        return "redirect:/searchByPhoneNumber";
    }


    @GetMapping("/search-by-email-form")
    public String searchByEmailForm() {
        return "search-by-email-form";
    }
    @PostMapping("/searchByEmail")
    public String searchByEmail(@RequestParam String email,
                                      RedirectAttributes redirectAttributes, HttpSession session) {
        Account accountByEmail = AccountStorage.getAccounts().stream()
                .filter(account -> account.getEmail().equals(email))
                .findFirst().orElse(null);
        if (accountByEmail == null) {
            redirectAttributes.addFlashAttribute("error", "Incorrect email");
            return "redirect:/search-by-email-form";
        }
        session.setAttribute("accountByEmail", accountByEmail);
        return "redirect:/account-by-email-page";
    }
    @GetMapping("/account-by-email-page")
    public String accountByEmailPage(Model model, HttpSession session) {
        Account accountByEmail = (Account) session.getAttribute("accountByEmail");
        if (accountByEmail != null) {
            model.addAttribute("accountByEmail", accountByEmail);
            return "account-by-email-page";
        }
        return "redirect:/searchByEmail";
    }


    @GetMapping("/login-form")
    public String loginForm() {
        return "login-form";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login, @RequestParam String password,
                        RedirectAttributes redirectAttributes, HttpSession session) {
        Account loginToAccount = AccountStorage.getAccounts().stream()
                .filter(account -> account.getLogin().equals(login))
                .findFirst().orElse(null);
        if (loginToAccount == null || !loginToAccount.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect login or password");
            return "redirect:/login-form";
        }
        session.setAttribute("loginToAccount", loginToAccount);
        return "redirect:/client-form";
    }

    @GetMapping("/client-form")
    public String clientForm(Model model, HttpSession session) {
        Account loginToAccount = (Account) session.getAttribute("loginToAccount");
        if (loginToAccount != null) {
            model.addAttribute("loginToAccount", loginToAccount);
            return "client-form";
        }
        return "redirect:/login";
    }
    @GetMapping("/edit-client-form/{id}")
    public String editClientForm(@PathVariable("id") String id, Model model) {
        Account accountToEdit = AccountStorage.getAccounts().stream().
                filter(account -> account.getId().equals(id)).
                findFirst().orElseThrow(RuntimeException::new);
        model.addAttribute("account", accountToEdit);
        return "edit-client-form";
    }
    @PostMapping("/updateClient")
    public String updateClient(Account account, Model model, HttpSession session) {
        delete(account.getId());
        AccountStorage.getAccounts().add(account);
        return "redirect:/login-form";
    }

    @GetMapping("/transfer-by-phone-number-form/{id}")
    public String transferByPhoneNumberForm(@PathVariable("id") String id, Model model) {
            Account accountFrom = AccountStorage.getAccounts().stream().
                    filter(account -> account.getId().equals(id)).
                    findFirst().orElseThrow(RuntimeException::new);
            model.addAttribute("accountFrom", accountFrom);
        return "transfer-by-phone-number-form";
    }
    @PostMapping("/transfer") public String transfer(
            @RequestParam("fromAccountId") String fromAccountId,
            @RequestParam("toPhoneNumber") String toPhoneNumber,
            @RequestParam("amount") double amount) {
        Account accountFrom = AccountStorage.getAccounts().stream()
                .filter(account -> account.getId().equals(fromAccountId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));

        Account accountTo = AccountStorage.getAccounts().stream()
                .filter(account -> account.getTelephoneNumber().equals(toPhoneNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        if (accountFrom.getAmount() >= amount) {
            accountFrom.withdraw(amount);
            accountTo.deposit(amount);
            return "redirect:/client-form";
        } else {
            return "redirect:/transfer-by-phone-number-form/" + fromAccountId;
        }
    }

}
