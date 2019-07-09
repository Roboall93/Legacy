/* ATOMIC MASSES DATA SET
*   Shaun Donnelly
*   main function to read in the .dat file
*   and assign the values to three arrays for proton, atomic and mass numbers
*   used to contain each individual atomic mass
*
*   NOTE: each mass is stored at in array with index [i] where i is an integer
*   i.e. massNums[0] would return 1.00783
*/


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define DATA_SET    "mass_data.dat" // defining filename for ease of reference

/* A structure to hold data for each atomic mass
    it contains the proton number and atomic number as integers,
    and the mass number as a float to conserve the decimal
*/

int main()
{
    /* declared variables */
    FILE *file; // stores our .dat file
    char buffer[256], *token; // 'strings' to hold each line of the file, and for when we split up those lines into tokens
    int protNums[3180]; // array to hold our proton numbers (Z)
    int atomNums[3180]; // array to hold our atomic numbers
    float massNums[3180]; // array to hold our mass numbers (A)
    int neutronNums[3180]; //array to hold our calculated nuetron numbers (A-Z)
    float sepEnergy[3180]; // array to hold cluster separation energy
    int i = 0; // counter to increment array

    /* opening file for reading */
    file = fopen(DATA_SET,"r");
    /* simple error if file not found */
    if(!file)
    {
        perror("Error opening file");
        return -1;
    }

    /*
    loop until we get to the end of the file
    during each loop we take a line from the file,
    split this by whitespace to get each number,
    then convert that to the appropriate datatype
    to store in the arrays
    */
    while(fgets(buffer, 256, file) != NULL)  { // get next line of the file
        token = strtok(buffer, " "); // get the next part of the line
        protNums[i] = atoi(token); // convert the characters to an int
        token = strtok(NULL, " "); // advance past the next whitespace
        atomNums[i] = atoi(token); // convert again
        token = strtok(NULL, " "); // advance
        massNums[i] = (float)atof(token); // convert to float (conserve decimal places)

        // use this print statement to confirm the correct values are being written to each loop of the array
        //printf("Testing Array index %d: %d %d %f\n", i, protNums[i], atomNums[i], massNums[i]);

        i++; // advance counter, VERY IMPORTANT

}
        // print statements for testing if a specific index is correct (compare to data file)
        //printf("Testing Array index 2: %d %d %f\n", protNums[1], atomNums[1], massNums[1]);
        printf("Dataset Read.\n");


        //Other tasks
        //(b), neutron numbers
        i = 0;
        for (i; i < 3180; i++) { // loop through whole list
            neutronNums[i] = atomNums[i] - protNums[i]; // N = A - Z
        }
        printf("Neutron Numbers Calculated.\n");


        //(c), writing neutron numbers to a file
        FILE * massDataNeutrons;
        // open the file for writing
        massDataNeutrons = fopen ("mass_data_neutrons.dat","w+");
        int j = 0;
        for (j; j < 3180; j++){
            fprintf(massDataNeutrons,"%d, %d, %f, %d\n", protNums[j], atomNums[j], massNums[j], neutronNums[j]); // print the lists, add return
        }
        //close the file
        fclose (massDataNeutrons);
        printf("Neutron Numbers File Created.\n");


        //(d), specific cluster separation energy
        int clusterProt;
        int clusterNeut;
        printf("Calculating Cluster Separation Energy:\n");
        printf("Enter number of cluster protons: ");
        scanf("%d", &clusterProt);
        printf("Enter number of cluster neutrons: ");
        scanf("%d", &clusterNeut);

        float clusterMass = 0;
        // Loop through dataset to find matching cluster
        int k = 0;
        for (k; k < 3180; k++) {
            if (protNums[k] == clusterProt && neutronNums[k] == clusterNeut){
                clusterMass = massNums[k];
            }
        }
        // print error if no appropriate cluster found
        if (clusterMass == 0){
            printf("Matching cluster not found.");
            return 0;
        }

        // Value holders
        float parentMass = 0;
        int daughterProt = 0;
        int daughterNeut = 0;
        float daughterMass = 0.0;
        // Loop through all parents and find daughters
        int l = 0;
        int m = 0;
        for (l; l < 3180; l++) {
            // Check if cluster is bigger than parent
            if (protNums[l] <= clusterProt || neutronNums[l] <= clusterNeut){
                continue; // skip to next loop
            }
            parentMass = massNums[l];
            daughterProt = protNums[l] - clusterProt;
            daughterNeut = neutronNums[l] - clusterNeut;
            // Find mass of daughter
            for (m; m < 3180; m++) {
                if (protNums[m] == daughterProt && neutronNums[m] == daughterNeut){
                    daughterMass = massNums[m];
                }
            }
            // Calculate separation energy
            sepEnergy[l] = parentMass - daughterMass + clusterMass;
        }



        printf("Separation Energy calculated.\n");
        /* Testing print statements
        for (int i = 0; i < 10; i++) {
            printf("%f\n", sepEnergy[i]);
        }
        */

        //(e), writing separation energy to a file
        FILE *massDataSeparation;
        // open the file for writing
        massDataSeparation = fopen ("mass_data_separation.dat","w+");
        int n = 0;
        for (n; n < 3180; n++){
            // check if separation energy is 0
            if (sepEnergy[n] == 0){
                fprintf(massDataSeparation,"%d, %d, %f, %d\n", protNums[n], atomNums[n], massNums[n], neutronNums[n]); // print without separation energy
            } else {
                fprintf(massDataSeparation,"%d, %d, %f, %d, %f\n", protNums[n], atomNums[n], massNums[n], neutronNums[n], sepEnergy[n]); // print the lists, add return
            }

        }
        //close the file
        fclose (massDataNeutrons);
        printf("Separation Energy File Created.\n");



    return 0;
}
