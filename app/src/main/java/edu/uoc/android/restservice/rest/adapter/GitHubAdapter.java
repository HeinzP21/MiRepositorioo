package edu.uoc.android.restservice.rest.adapter;

import java.util.List;

import edu.uoc.android.restservice.rest.contants.ApiConstants;
import edu.uoc.android.restservice.rest.model.Owner;
import edu.uoc.android.restservice.rest.model.Followers;
import edu.uoc.android.restservice.rest.service.GitHubService;
import retrofit2.Call;

public class GitHubAdapter extends BaseAdapter implements GitHubService {

    private GitHubService gitHubService;

    public GitHubAdapter() {
        super(ApiConstants.BASE_GITHUB_URL);
        gitHubService = createService(GitHubService.class);
    }

    @Override
    public Call<Owner> getOwner(String owner) {
        return gitHubService.getOwner(owner);
    }

    @Override
    public Call<List<Followers>> getOwnerFollowers(String owner) { // Nos permite obtener la lista de seguidores del usuario
        return gitHubService.getOwnerFollowers(owner);
    }
}
